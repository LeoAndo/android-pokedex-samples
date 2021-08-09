package com.example.pokedexkotlinsample.presentation.util

import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineExceptionHandler

internal class PokemonExceptionHandler(
    private val fragment: Fragment,
    override val onUnAuthorizedAction: () -> Unit
) : RetryConnectionHandler(), UnAuthorizedErrorHandleable,
    NetworkConnectionErrorHandleable {
    internal fun handleError(throwable: Throwable) {
        if (super.handleUnAuthorizedError(fragment, throwable)) return
        if (super.handleNetworkConnectionError(listener, fragment, throwable)) return

        // default
        Toast.makeText(
            fragment.requireContext(),
            throwable.localizedMessage,
            Toast.LENGTH_SHORT
        ).show()
    }

    internal val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }
}