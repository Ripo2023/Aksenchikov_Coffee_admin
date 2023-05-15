package com.example.coffee_admin.presentation.MainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.coffee_admin.presentation.MainScreen.models.MainScreenState
import com.example.coffee_admin.presentation.MainScreen.models.ProductInfoDialogState
import com.example.myapplicationnew.presentation.OrderScreen.models.OrderViewModel
import com.google.firebase.components.Lazy

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    mainScreenViewModel: MainScreenViewModel = hiltViewModel()
) {
    val mainScreenState by mainScreenViewModel.mainScreenState.collectAsState()

    val productInfoDialogState by mainScreenViewModel.productInfoDialogState.collectAsState()

    val isRefresh = mainScreenViewModel.isRefresh.collectAsState()

    val pullRefreshStateState = rememberPullRefreshState(
        refreshing = isRefresh.value,
        onRefresh = { mainScreenViewModel.reload() }
    )

    Box(
        Modifier
            .fillMaxWidth()
            .pullRefresh(pullRefreshStateState),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshStateState)
        ) {
            when(mainScreenState) {
                is MainScreenState.Loading -> item {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                }
                is MainScreenState.OrderList -> {
                    if((mainScreenState as MainScreenState.OrderList).orders.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = "На данный момент заказов нет")
                            }
                        }
                    } else {
                        items((mainScreenState as MainScreenState.OrderList).orders) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFFF5712))
                                    .clickable { mainScreenViewModel.showProductInfoDialog(it) }
                                ,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = it.photoImage,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(end = 60.dp)
                                        .size(40.dp)
                                        .clip(CircleShape)
                                )
                                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                                    Text(
                                        text = it.id,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.W800
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        PullRefreshIndicator(refreshing = isRefresh.value,
            state = pullRefreshStateState)
    }


    if(productInfoDialogState is ProductInfoDialogState.Show) {
        ProductInfoDialog((productInfoDialogState as ProductInfoDialogState.Show).order,mainScreenViewModel)
    }
}

@Composable
fun ProductInfoDialog(order: OrderViewModel,mainScreenViewModel: MainScreenViewModel) {
    Dialog(onDismissRequest = { mainScreenViewModel.hideProductInfoDialog() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SubcomposeAsyncImage(
                    model = order.photoImage,
                    contentDescription = "",
                    modifier = Modifier.size(150.dp)
                )

                Text(text = "id:${order.id}")
                Text(text = "Название заказа:${order.name}")

                Button(
                    onClick = { mainScreenViewModel.markCompleted(order.id) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFFF5712)
                    )
                ) {
                    Text(text = "Отметить выполненым")
                }
            }
        }
    }
}