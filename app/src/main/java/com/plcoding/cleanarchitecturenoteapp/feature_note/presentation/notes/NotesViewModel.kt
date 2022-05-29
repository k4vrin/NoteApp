package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case.NoteUseCases
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotesViewModel @Inject constructor(
	private val noteUseCases: NoteUseCases
) : ViewModel() {

	private val _state = mutableStateOf(NotesState())
	val state: State<NotesState> = _state

	private var recentlyDeleteNote: Note? = null

	// To keep a reference of the job and cancel it when it's a new call to getNotes function
	private var getNotesJob: Job? = null

	init {
		getNotes(noteOrder = NoteOrder.Date(OrderType.Descending))
	}

	fun onEvent(event: NotesEvent) {
		when (event) {

			// Ordering
			is NotesEvent.Order -> {
				// If the order that is checked already, equals to order that is being asked we don't want to do anything
				// If we check noteOrder without class it will just check referential equality. because it's not data class
				if (state.value.noteOrder::class == event.noteOrder::class &&
					// Here we can check without class because it's an object
					state.value.noteOrder.orderType == event.noteOrder.orderType
				) {
					return
				}
				// If we didn't return
				getNotes(event.noteOrder)
			}

			// Delete
			is NotesEvent.DeleteNote -> {
				// Tell use case to delete the note. Use case tell repo
				viewModelScope.launch {
					// Because delete note use case invoke is operator that we override we can call it like function
					noteUseCases.deleteNoteUseCase(note = event.note)
					// Keep reference of last deleted note
					recentlyDeleteNote = event.note
				}
			}

			// Restore
			is NotesEvent.RestoreNote -> {
				viewModelScope.launch {
					noteUseCases.addNoteUseCase(note = recentlyDeleteNote ?: return@launch)
					recentlyDeleteNote = null
				}
			}

			// Toggle order section
			is NotesEvent.ToggleOrderSection -> {
				// Whatever the state is we just toggle the order section visibility
				_state.value = state.value.copy(
					isOrderSectionVisible = !state.value.isOrderSectionVisible
				)
			}
		}
	}

	// Every time we call this function we get new instance of the flow
	// so when we call this function we want to cancel the old coroutine that is observing the database
	private fun getNotes(noteOrder: NoteOrder) {
		getNotesJob?.cancel()
		getNotesJob = noteUseCases.getNotesUseCase(noteOrder = noteOrder)
			// On each emission map notes to state
			.onEach { notes ->
				_state.value = state.value.copy(
					notes = notes,
					noteOrder = noteOrder
				)
			}
			.launchIn(scope = viewModelScope)
	}
}


