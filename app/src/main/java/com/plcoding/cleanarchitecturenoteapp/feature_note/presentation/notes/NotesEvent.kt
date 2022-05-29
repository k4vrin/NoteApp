package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes

import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder

/**
 * Notes event
 *
 * @constructor Create empty Notes event
 *
 * User actions
 */
sealed class NotesEvent {
	data class Order(val noteOrder: NoteOrder) : NotesEvent()
	data class DeleteNote(val note: Note) : NotesEvent()
	object RestoreNote : NotesEvent()
	object ToggleOrderSection : NotesEvent()
}
