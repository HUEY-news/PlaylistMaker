package com.practicum.playlistmaker.presentation.library

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import java.io.File
import java.io.FileOutputStream

class LibraryNewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private var albumCoverUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editFieldPlaylistName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val title = s.toString()
                binding.buttonCreate.isEnabled = title.isNotEmpty()
            }
        })

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    albumCoverUri = uri
                    binding.playlistImageAdd.setImageURI(uri)
                    binding.playlistImageAdd.scaleType = ImageView.ScaleType.CENTER_CROP
                } else {
                    Toast.makeText(requireContext(), "ERROR: no media selected", Toast.LENGTH_SHORT).show()
                }
            }

        binding.playlistImageAdd.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonBack.setOnClickListener { findNavController().navigateUp() }
        binding.buttonCreate.setOnClickListener {
            albumCoverUri?.let { saveImageToPrivateStorage(it) }
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "albums")
        if (!filePath.exists()) filePath.mkdirs()
        val file = File(filePath, binding.editFieldPlaylistName.text.toString())
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }
}