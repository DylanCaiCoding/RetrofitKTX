@file:Suppress("unused")

package com.dylanc.retrofit.coroutines

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.dylanc.retrofit.coroutines.exception.defaultExceptionObserver
import com.dylanc.retrofit.coroutines.loading.defaultLoadingObserver
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KClass


@MainThread
inline fun <reified VM : RequestViewModel> FragmentActivity.requestViewModels(
  observeLoading: Boolean = true,
  observeException: Boolean = true,
  noinline factoryProducer: () -> ViewModelProvider.Factory = { defaultViewModelProviderFactory },
): Lazy<VM> =
  RequestViewModelLazy(VM::class, { this }, { this },
    { viewModelStore }, factoryProducer, observeLoading, observeException
  )

@MainThread
inline fun <reified VM : RequestViewModel> Fragment.requestViewModels(
  observeLoading: Boolean = true,
  observeException: Boolean = true,
  noinline factoryProducer: () -> ViewModelProvider.Factory = { defaultViewModelProviderFactory },
): Lazy<VM> =
  RequestViewModelLazy(VM::class, { viewLifecycleOwner }, { requireActivity() },
    { viewModelStore }, factoryProducer, observeLoading, observeException
  )

class RequestViewModelLazy<VM : RequestViewModel>(
  private val viewModelClass: KClass<VM>,
  private val lifecycleOwnerProducer: () -> LifecycleOwner,
  private val activityProducer: () -> FragmentActivity,
  private val storeProducer: () -> ViewModelStore,
  private val factoryProducer: () -> ViewModelProvider.Factory,
  private val observeLoading: Boolean = true,
  private val observeException: Boolean = true,
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
          if (observeLoading) {
            defaultLoadingObserver?.let { observer ->
              lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onCreate(owner: LifecycleOwner) {
                  observer.onCreate(activity)
                }

                override fun onDestroy(owner: LifecycleOwner) {
                  observer.onDestroy()
                }
              })
              vm.isLoading.flowWithLifecycle(lifecycleOwner.lifecycle)
                .onEach { observer.onChanged(activity, it) }
                .launchIn(lifecycleOwner.lifecycleScope)
            }
          }
          if (observeException) {
            vm.exception.flowWithLifecycle(lifecycleOwner.lifecycle)
              .onEach { defaultExceptionObserver.onChanged(activity, it) }
              .launchIn(lifecycleOwner.lifecycleScope)
          }
          cached = vm
        }
      } else {
        viewModel
      }
    }

  override fun isInitialized() = cached != null
}
