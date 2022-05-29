package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.plcoding.cleanarchitecturenoteapp.R
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.OrderType

@Composable
fun OrderSection(
	modifier: Modifier = Modifier,
	noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
	onOrderChange: (NoteOrder) -> Unit
) {
	
	Column(
		modifier = modifier
	) {

		/******************************
		******** First Row **********
		*******************************/
		Row(
			modifier = Modifier
			    .fillMaxWidth()
		) {
			//// Title ////
			DefaultRadioButton(
				text = stringResource(R.string.title),
				selected = noteOrder is NoteOrder.Title,
				onSelect = {
					onOrderChange(NoteOrder.Title(noteOrder.orderType))
				}
			)
			//// Spacer ////
			Spacer(modifier = Modifier.width(8.dp))

			//// Date ////
			DefaultRadioButton(
				text = stringResource(R.string.date),
				selected = noteOrder is NoteOrder.Date,
				onSelect = {
					onOrderChange(NoteOrder.Date(noteOrder.orderType))
				}
			)

			//// Spacer ////
			Spacer(modifier = Modifier.width(8.dp))

			//// Color ////
			DefaultRadioButton(
				text = stringResource(R.string.color),
				selected = noteOrder is NoteOrder.Color,
				onSelect = {
					onOrderChange(NoteOrder.Color(noteOrder.orderType))
				}
			)
		}

		//// Spacer Vertical ////
		Spacer(modifier = Modifier.height(16.dp))

		/******************************
		******** Second Row **********
		*******************************/
		Row(
			modifier = Modifier
			    .fillMaxWidth()
		) {

			//// Ascending ////
			DefaultRadioButton(
				text = stringResource(R.string.ascending),
				selected = noteOrder.orderType is OrderType.Ascending,
				onSelect = {
					onOrderChange(noteOrder.copy(orderType = OrderType.Ascending))
				}
			)

			//// Spacer ////
			Spacer(modifier = Modifier.width(8.dp))

			//// Descending ////
			DefaultRadioButton(
				text = stringResource(R.string.descending),
				selected = noteOrder.orderType is OrderType.Descending,
				onSelect = {
					onOrderChange(noteOrder.copy(orderType = OrderType.Descending))
				}
			)
			
		}

	}
}