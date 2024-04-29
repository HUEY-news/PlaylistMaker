package com.practicum.playlistmaker.presentation.library

import android.app.AlertDialog
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
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.domain.library.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class LibraryNewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<LibraryNewPlaylistViewModel>()

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var coverImageUri: Uri? = null
    private var coverImageName: String = ""
    private var coverImageDescription: String = ""
    private var playlistId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { playlistId = it.getInt(PLAYLIST_ID) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlaylistFromLibrary(playlistId)
        viewModel.observeCurrentPlaylist().observe(viewLifecycleOwner) { playlist -> updateUI(playlist)}

        binding.editFieldPlaylistName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                coverImageName = s.toString()
                binding.buttonCreate.isEnabled = coverImageName.isNotEmpty()
            }
        })

        binding.editFieldPlaylistDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                coverImageDescription = s.toString()
            }
        })

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                uri?.let {
                    coverImageUri = uri
                    binding.playlistImageAdd.setImageURI(uri)
                    binding.playlistImageAdd.scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }

        binding.playlistImageAdd.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { _, _ -> }
            .setNegativeButton("Завершить") { _, _-> findNavController().navigateUp() }

        binding.buttonBack.setOnClickListener { checkDialog() }
        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { checkDialog() }
        })

        binding.buttonCreate.setOnClickListener {
            coverImageUri?.let { saveImageToPrivateStorage(it) }
            viewModel.createNewPlaylist(name = coverImageName, description = coverImageDescription, cover = coverImageUri)
            Toast.makeText(requireContext(), "Плейлист [ $coverImageName ] создан", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun updateUI(playlist: Playlist) {
        binding.textViewHeader.text = "Редактировать"
        binding.buttonCreate.text = "Сохранить"
        binding.editFieldPlaylistName.setText(playlist.playlistName)
        binding.editFieldPlaylistDescription.setText(playlist.playlistDescription)

        if (playlist.playlistCoverUri != null) {
            coverImageUri = playlist.playlistCoverUri.toUri()

            Glide
                .with(requireContext())
                .load(playlist.playlistCoverUri)
                .placeholder(R.drawable.button_add_photo_image)
                .centerCrop()
                .into(binding.playlistImageAdd)
        }

        binding.buttonCreate.setOnClickListener {
            coverImageUri?.let { saveImageToPrivateStorage(it) }
            viewModel.updatePlaylist(id = playlistId!!, name = coverImageName, description = coverImageDescription, cover = coverImageUri)
            Toast.makeText(requireContext(), "Плейлист [ $coverImageName ] сохранён", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PlaylistMaker")
        if (!filePath.exists()) filePath.mkdirs()
        val file = File(filePath, coverImageName)
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun checkDialog() {
        if (coverImageUri != null || coverImageName.isNotEmpty() || coverImageDescription.isNotEmpty()) {
            val dialog = confirmDialog.show()
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.YP_Blue))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.YP_Blue))
        } else findNavController().navigateUp()
    }

    companion object {
        private const val PLAYLIST_ID = "PLAYLIST_ID"

        fun createBundle(playlistId: Int) = Bundle().apply {
            putInt(PLAYLIST_ID, playlistId)
        }
    }
}