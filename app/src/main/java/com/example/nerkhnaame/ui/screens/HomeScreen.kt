package com.example.nerkhnaame.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nerkhnaame.R
import com.example.nerkhnaame.data.remote.model.Gold
import com.example.nerkhnaame.data.remote.model.GoldAnalysisItem
import com.example.nerkhnaame.ui.theme.BackViewBlack
import com.example.nerkhnaame.ui.theme.GoldText
import com.example.nerkhnaame.ui.theme.WhiteText
import com.example.nerkhnaame.viewModel.AnalysisState
import com.example.nerkhnaame.viewModel.AnalysisViewModel
import com.example.nerkhnaame.viewModel.HomeState
import com.example.nerkhnaame.viewModel.HomeViewModel
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
fun Home(
    homeViewModel: HomeViewModel = hiltViewModel(),
    analysisViewModel: AnalysisViewModel = hiltViewModel()
) {

    val homeState by homeViewModel.state.collectAsState()
    val analysisState by analysisViewModel.state.collectAsState()

    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 1)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackViewBlack)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.back_top_main),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Text(
                text = homeState.todayDate,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                style = TextStyle(
                    textDirection = TextDirection.Rtl,
                    textAlign = TextAlign.Center
                )
            )
        }

        Spacer(Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0xff262523), RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                CategoryButton(
                    text = stringResource(R.string.tab_analysis),
                    isSelected = pagerState.currentPage == 0,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(0) }
                    }
                )

                Spacer(Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .height(25.dp)
                        .width(2.dp)
                        .background(Color.White, RoundedCornerShape(12.dp))
                )

                Spacer(Modifier.width(10.dp))

                CategoryButton(
                    text = stringResource(R.string.tab_price),
                    isSelected = pagerState.currentPage == 1,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(1) }
                    }
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->

            when (page) {

                0 -> AnalysisScreen(
                    state = analysisState,
                    analysisItems = analysisState.allItems(),
                    onRefresh = { analysisViewModel.fetchAnalysis() }
                )

                1 -> PriceListScreen(
                    state = homeState,
                    onRefresh = { homeViewModel.getGolds() }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceListScreen(
    state: HomeState,
    onRefresh: () -> Unit
) {

    val pullState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = onRefresh,
        state = pullState,
        modifier = Modifier.fillMaxSize()
    ) {

        when {

            state.isLoading && state.golds.isEmpty() -> {
                LoadingView()
            }

            state.error != null && state.golds.isEmpty() -> {
                ErrorView(onRetry = onRefresh)
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(state.golds) {
                        ListPriceItem(it)
                    }
                }
            }
        }
    }
}

@Composable
fun ListPriceItem(gold: Gold) {

    val icon =
        if (gold.name_en.contains("Gold", ignoreCase = true))
            R.drawable.ic_gold
        else
            R.drawable.ic_coin


    val formattedPrice = remember(gold.price) {
        String.format(Locale.US, "%,d", gold.price)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xff1c1b1a))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.Bottom) {

                Text(
                    text = gold.unit ?: "",
                    fontSize = 13.sp,
                    color = Color.LightGray
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    text = formattedPrice,
                    fontSize = 18.sp,
                    color = GoldText,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = gold.name ?: "",
                fontSize = 17.sp,
                color = WhiteText,
                fontWeight = FontWeight.SemiBold
            )

            Image(
                painter = painterResource(icon),
                contentDescription = gold.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF454545))
                    .padding(8.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    analysisItems: List<GoldAnalysisItem>,
    state: AnalysisState,
    onRefresh: () -> Unit
) {

    val pullState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = onRefresh,
        state = pullState,
        modifier = Modifier.fillMaxSize()
    ) {

        when {

            state.isLoading && analysisItems.isEmpty() -> {
                LoadingView()
            }

            state.error != null && analysisItems.isEmpty() -> {
                ErrorView(onRetry = onRefresh)
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    itemsIndexed(analysisItems) { _, item ->
                        AnalysisCard(item)
                    }
                }
            }
        }
    }
}

@Composable
fun AnalysisCard(item: GoldAnalysisItem) {

    val adviceColor = when (item.advice.action) {
        "BUY" -> Color(0xFF27ae60)
        "SELL" -> Color(0xFFe74c3c)
        else -> Color(0xFFf1c40f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xff1c1b1a))
            .padding(16.dp)
    ) {

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {


            Text(
                text = item.title ?: "",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = WhiteText
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(adviceColor.copy(alpha = 0.2f))
                    .padding(10.dp)
            ) {

                Column {

                    Text(
                        text = item.advice.title ?: "",
                        color = adviceColor,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = item.advice.description ?: "",
                        color = WhiteText.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                val price = item.market_stats?.current_price ?: "نامشخص"
                val change = item.market_stats?.change_percent ?: "--"

                Text(
                    text = "قیمت: $price",
                    color = WhiteText,
                    fontSize = 14.sp
                )

                Text(
                    text = "تغییر: $change",
                    color = adviceColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

            if (item.chart_points.isNotEmpty()) {
                MiniLineChart(item.chart_points)
            }
        }
    }
}

@Composable
fun MiniLineChart(points: List<Long>) {

    val max = points.maxOrNull()?.toFloat() ?: 1f
    val min = points.minOrNull()?.toFloat() ?: 0f
    val diff = max - min

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF262523))
    ) {

        if (points.size < 2) return@Canvas

        val stepX = size.width / (points.size - 1)
        val scaleY = if (diff != 0f) size.height / diff else 1f

        for (i in 0 until points.size - 1) {

            val startX = i * stepX
            val startY = size.height - (points[i] - min) * scaleY

            val stopX = (i + 1) * stepX
            val stopY = size.height - (points[i + 1] - min) * scaleY

            drawLine(
                color = GoldText,
                start = Offset(startX, startY),
                end = Offset(stopX, stopY),
                strokeWidth = 4f
            )
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = GoldText)
    }
}

@Composable
fun ErrorView(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = stringResource(R.string.error_fetch_data),
                color = Color.Red,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = GoldText)
            ) {
                Text(text = stringResource(R.string.error_text_btn), color = Color.Black)
            }
        }
    }
}

@Composable
private fun CategoryButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {

    val textColor = if (isSelected) GoldText else Color.Gray

    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            style = TextStyle(textAlign = TextAlign.Center),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
