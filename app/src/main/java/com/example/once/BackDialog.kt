package com.example.once

import androidx.fragment.app.DialogFragment

class BackDialog(backDialogInterface: BackDialogInterface,
                 text: String, id: Int) : DialogFragment() {

    private var _binding: BackDialog = null
    private val binding get() = _binding!!

    private var confirmDialogInterface: ConfirmDialogInterface? = null

    private var text: String? = null
    private var id: Int? = null

    init {
        this.text = text
        this.id = id
        this.confirmDialogInterface = confirmDialogInterface
    }
}

interface BackDialogInterface {
    fun onYesButtonClick(id: Int)
}