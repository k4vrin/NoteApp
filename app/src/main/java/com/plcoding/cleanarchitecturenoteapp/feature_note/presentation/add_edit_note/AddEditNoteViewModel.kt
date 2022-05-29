package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.InvalidNoteException
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
	private val noteUseCases: NoteUseCases,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	//// Note Title ////
	private val _noteTitle = mutableStateOf(NoteTextFieldState(
		hint = "Enter title..."
	))
	val noteTitle: State<NoteTextFieldState> = _noteTitle

	//// Note Content ////
	private val _noteContent = mutableStateOf(NoteTextFieldState(
		hint = "Enter some content"
	))
	val noteContent: State<NoteTextFieldState> = _noteContent

	//// Note Color ////
	private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
	val noteColor: State<Int> = _noteColor

	//// UiEvents ////
	private val _eventFlow = MutableSharedFlow<UiEvent>()
	val eventFlow = _eventFlow.asSharedFlow()

	//// Current Id ////
	private var currentNoteId: Int? = null

	init {
		// Get note in the beginning
		savedStateHandle.get<Int>("noteId")?.let { noteId ->
			//// Edit ////
			if (noteId != -1) {
				viewModelScope.launch {
					// Get the note by id and update states
					noteUseCases.getNoteUseCase(id = noteId)?.also { note ->
						currentNoteId = note.id

						_noteTitle.value = noteTitle.value.copy(
							text = note.title,
							isHintVisible = false
						)

						_noteContent.value = noteContent.value.copy(
							text = note.title,
							isHintVisible = false
						)

						_noteColor.value = note.color
					}
				}
			}
			//// Add ////
			// Add would be the default state
		}
	}


	/**
	 * On event
	 *
	 * @param event
	 *
	 * Actions for each event
	 */
	fun onEvent(event: AddEditNoteEvent) {
		when (event) {

			//// Title Focus ////
			is AddEditNoteEvent.ChangeTitleFocus -> {
				_noteTitle.value = noteTitle.value.copy(
					isHintVisible = !event.focusState.isFocused &&
							noteTitle.value.text.isBlank()
				)
			}

			//// Title Text ////
			is AddEditNoteEvent.EnteredTitle -> {
				_noteTitle.value = noteTitle.value.copy(text = event.value)
			}

			//// Content Focus ////
			is AddEditNoteEvent.ChangeContentFocus -> {
				_noteContent.value = noteContent.value.copy(
					isHintVisible = !event.focusState.isFocused &&
							noteContent.value.text.isBlank()
				)
			}

			//// Content Text ////
			is AddEditNoteEvent.EnteredContent -> {
				_noteContent.value = noteContent.value.copy(text = event.value)
			}

			//// Color ////
			is AddEditNoteEvent.ChangeColor -> {
				_noteColor.value = event.color
			}

			//// Save Note ////
			is AddEditNoteEvent.SaveNote -> {
				viewModelScope.launch {
					try {
						noteUseCases.addNoteUseCase(
							note = Note(
								title = noteTitle.value.text,
								content = noteContent.value.text,
								timestamp = System.currentTimeMillis(),
								color = noteColor.value,
								id = currentNoteId
							)
						)
						//// Save note(Success) ////
						_eventFlow.emit(UiEvent.SaveNote)
					} catch (e: InvalidNoteException) {
						//// Show Snackbar(Error) ////
						_eventFlow.emit(
							UiEvent.ShowSnackbar(
								message = e.message ?: "Couldn't save note"
							)
						)
					}
				}
			}

		}
	}


	/**
	 * Ui event
	 *
	 * @constructor Create empty Ui event
	 *
	 * Show Snackbar when there is an error otherwise save the note
	 */
	sealed class UiEvent {
		data class ShowSnackbar(val message: String) : UiEvent()
		object SaveNote : UiEvent()
	}


}