//package com.mesapronta.app.component
//
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Card
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun <T> ViewList(
//    items: List<T>,
//    onItemClick: (T) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    LazyColumn(modifier = modifier) {
//        items(items) { item ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                onClick = { onItemClick(item) }
//            ) {
//                Text(
//                    text = item.toString(),
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//        }
//    }
//}