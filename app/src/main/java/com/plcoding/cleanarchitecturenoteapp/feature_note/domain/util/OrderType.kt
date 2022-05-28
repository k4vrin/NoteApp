package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util

/**
 * Order type
 *
 * ViewModels use this to tell the use case what type of order they want. Ascending or Descending
 */
sealed class OrderType {
	object Ascending: OrderType()
	object Descending: OrderType()
}
