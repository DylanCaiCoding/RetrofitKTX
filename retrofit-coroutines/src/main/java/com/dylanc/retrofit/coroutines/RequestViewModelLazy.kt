@file:Suppress("unused")

package com.dylanc.retrofit.coroutines

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.dylanc.retrofit.coroutines.livedata.LoadingObserver
import kotlin.reflect.KClass


@MainThread
inline fun <reified VM : RequestViewModel> FragmentActivity.requestViewModels(
  loadingObserver: Observer<Boolean>? = null,
  exceptionObserver: Observer<Throwable>? = null,
  noinline factoryProducer: () -> ViewModelProvider.Factory = { defaultViewModelProviderFactory },
): Lazy<VM> =
  RequestViewModelLazy(VM::class, this, supportFragmentManager, { viewModelStore }, factoryProducer, loadingObserver, exceptionObserver)

@MainThread
inline fun <reified VM : RequestViewModel> Fragment.requestViewModels(
  loadingObserver: Observer<Boolean>? = null,
  exceptionObserver: Observer<Throwable>? = null,
  noinline factoryProducer: () -> ViewModelProvider.Factory = { defaultViewModelProviderFactory },
): Lazy<VM> =
  RequestViewModelLazy(VM::class, viewLifecycleOwner, parentFragmentManager, { viewModelStore }, factoryProducer, loadingObserver, exceptionObserver)

class RequestViewModelLazy<VM : RequestViewModel>(
  private val viewModelClass: KClass<VM>,
  private val lifecycleOwner: LifecycleOwner,
  private val fragmentManager: FragmentManager,
  private val storeProducer: () -> ViewModelStore,
  private val factoryProducer: () -> ViewModelProvider.Factory,
  private val loadingObserver: Observer<Boolean>?,
  private val exceptionObserver: Observer<Throwable>?,
) : Lazy<VM> {
  private var cached: VM? = null

  override val value: VM
    get() {
      val viewModel = cached
      return if (viewModel == null) {
        val factory = factoryProducer()
        val store = storeProducer()
        ViewModelProvider(store, factory).get(viewModelClass.java).also { vm ->
          (loadingObserver ?: defaultLoadingDialogFactory?.let { LoadingObserver(fragmentManager, it()) })?.let {
            vm.isLoading.observe(lifecycleOwner, it)
          }
          (exceptionObserver ?: defaultExceptionObserver)?.let {
            vm.exception.observe(lifecycleOwner, it)
          }
          cached = vm
        }
      } else {
        viewModel
      }
    }

  override fun isInitialized(): Boolean = cached != null
}
