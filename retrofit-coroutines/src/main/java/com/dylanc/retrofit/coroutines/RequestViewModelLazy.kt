@file:Suppress("unused")

package com.dylanc.retrofit.coroutines

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import kotlin.reflect.KClass


@MainThread
inline fun <reified VM : RequestViewModel> FragmentActivity.requestViewModels(
  loadingObserver: Observer<Boolean>? = null,
  exceptionObserver: Observer<Throwable>? = null,
  noinline factoryProducer: () -> ViewModelProvider.Factory = { defaultViewModelProviderFactory },
): Lazy<VM> =
  RequestViewModelLazy(VM::class, { this }, { this },
    { viewModelStore }, factoryProducer, loadingObserver, exceptionObserver
  )

@MainThread
inline fun <reified VM : RequestViewModel> Fragment.requestViewModels(
  loadingObserver: Observer<Boolean>? = null,
  exceptionObserver: Observer<Throwable>? = null,
  noinline factoryProducer: () -> ViewModelProvider.Factory = { defaultViewModelProviderFactory },
): Lazy<VM> =
  RequestViewModelLazy(VM::class, { viewLifecycleOwner }, { requireActivity() },
    { viewModelStore }, factoryProducer, loadingObserver, exceptionObserver
  )

class RequestViewModelLazy<VM : RequestViewModel>(
  private val viewModelClass: KClass<VM>,
  private val lifecycleOwnerProducer: () -> LifecycleOwner,
  private val activityProducer: () -> FragmentActivity,
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
        val activity = activityProducer()
        val lifecycleOwner = lifecycleOwnerProducer()
        ViewModelProvider(store, factory).get(viewModelClass.java).also { vm ->
          (loadingObserver ?: defaultLoadingObserver?.let {
            it.onCreate(activity)
            lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
              @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
              fun onCreate() {
                it.onCreate(activity)
              }

              @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
              fun onDestroy() {
                it.onDestroy()
              }
            })
            Observer<Boolean> { isShow -> it.onChanged(activity, isShow) }
          })?.let {
            vm.isLoading.observe(lifecycleOwner, it)
          }

          (exceptionObserver ?: Observer<Throwable> { throwable ->
            defaultErrorObserver(activity, throwable)
          }).let {
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
