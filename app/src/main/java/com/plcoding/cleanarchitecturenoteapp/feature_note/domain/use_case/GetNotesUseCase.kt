package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesUseCase(
	// It should be the interface to be replaceable in future if needed
	private val repository: NoteRepository
) {
	/**
	 * Invoke
	 *
	 * Call our class like function
	 */
	operator fun invoke(
		noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
	): Flow<List<Note>> {

		return repository.getNotes().map { notes ->

			when(noteOrder.orderType) {

				is OrderType.Ascending -> {
					when (noteOrder) {
						is NoteOrder.Title -> notes.sortedBy { note -> note.title.lowercase() }
						is NoteOrder.Date -> notes.sortedBy { note -> note.timestamp }
						is NoteOrder.Color -> notes.sortedBy { note -> note.color }
					}
				}

				is OrderType.Descending -> {
					when (noteOrder) {
						is NoteOrder.Title -> notes.sortedByDescending { note -> note.title.lowercase() }
						is NoteOrder.Date -> notes.sortedByDescending { note -> note.timestamp }
						is NoteOrder.Color -> notes.sortedByDescending { note -> note.color }
					}
				}
			}
		}
	}
}


/**
 *
 * UseCases contains business logic and they should have only one public function
 *
 */