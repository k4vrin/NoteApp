package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes

import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.OrderType

/**
 * Notes state
 *
 * @property notes
 * @property noteOrder
 * @property isOrderSectionVisible
 * @constructor Create empty Notes state
 *
 * Wrapping our states in a data class. Used in viewModel
 */
data class NotesState(
	// Notes
	val notes: List<Note> = emptyList(),
	// Radio buttons
	val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
	// Selecting Order Section
	val isOrderSectionVisible: Boolean = false
)
