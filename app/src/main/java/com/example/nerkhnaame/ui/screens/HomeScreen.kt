package com.example.nerkhnaame.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun Home(
    viewModel: HomeViewModel = hiltViewModel(),
    analysisViewModel: AnalysisViewModel = hiltViewModel()
) {

    val homeState by viewModel.state.collectAsState()
    val analysisState by analysisViewModel.state.collectAsState()

    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 1)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackViewBlack)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_top_main),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = homeState.todayDate,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = TextStyle(
                        textDirection = TextDirection.Rtl,
                        textAlign = TextAlign.Center
                    )
                )

                Text(
                    text = homeState.holiday,
                    fontSize = 18.sp,
                    color = Color.Black.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        textDirection = TextDirection.Rtl
                    )
                )
            }
        }

        Spacer(Modifier.height(25.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
                .background(Color(0xff262523), shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp),
        ) {

            val selectTabIndex = pagerState.currentPage

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(18.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                CategoryButton(
                    text = "تحلیل خرید یا فروش",
                    isSelected = selectTabIndex == 0,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }
                )

                Spacer(Modifier.width(15.dp))

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .height(22.dp)
                        .width(2.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                )

                Spacer(Modifier.width(15.dp))

                CategoryButton(
                    text = "قیمت طلا و سکه",
                    isSelected = selectTabIndex == 1,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                )

            }

        }


        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) {

            when (it) {
                0 -> {
                    AnalysisScreen(
                        analysisItems = analysisState.allItems(),
                        state = analysisState,
                        onRefresh = {
                            analysisViewModel.fetchAnalysis()
                        }
                    )
                }

                1 -> {
                    PriceListScreen(
                        homeState,
                        onRefresh = {
                            viewModel.getGolds()
                            viewModel.getHolidaysByDate()
                        }
                    )
                }
            }
        }

    }
}

@Composable
fun PriceListScreen(
    state: HomeState,
    onRefresh: () -> Unit
) {

    val statePullToRef = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = state.isLoading && state.golds.isNotEmpty(),
        onRefresh = onRefresh,
        state = statePullToRef,
        modifier = Modifier.fillMaxSize()
    ) {

        if (state.isLoading && state.golds.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GoldText)
            }
        } else if (state.error != null && state.golds.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GoldText)
            }

        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp)
            ) {

                items(state.golds) {
                    ListPriceItem(it)
                }

            }
        }
    }

}

@Composable
fun ListPriceItem(
    gold: Gold,
) {
    val cardBackground = Color(0xff1c1b1a)
    val iconBackground = Color(0xFF454545)
    val unitColor = Color.LightGray

    val icon = if (gold.name_en.contains(
            "Gold",
            ignoreCase = true
        )
    ) R.drawable.ic_gold else R.drawable.ic_coin
    val formattedPrice = String.format(Locale.US, "%,d", gold.price)



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(cardBackground)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Start
            ) {

                Text(
                    text = gold.unit,
                    fontSize = 13.sp,
                    color = unitColor,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(bottom = 1.dp)
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = formattedPrice,
                    fontSize = 18.sp,
                    color = GoldText,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = gold.name,
                    fontSize = 18.sp,
                    color = WhiteText,
                    fontWeight = FontWeight.SemiBold
                )

            }

            Image(
                painter = painterResource(icon),
                contentDescription = gold.name_en,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(iconBackground)
                    .padding(8.dp)
            )

        }
    }
}


@Composable
fun AnalysisScreen(
    analysisItems: List<GoldAnalysisItem>,
    state: AnalysisState,
    onRefresh: () -> Unit
) {

    val statePullToRef = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = state.isLoading && state.allItems().isNotEmpty(),
        onRefresh = {
            onRefresh()
        },
        state = statePullToRef,
        modifier = Modifier.fillMaxSize()
    ) {

        if (state.isLoading && state.allItems().isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GoldText)
            }
        } else if (state.error != null && state.allItems().isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GoldText)
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp)
            ) {
                itemsIndexed(analysisItems) { index, item ->
                    AnalysisCard(item)
                }
            }
        }
    }
}

@Composable
fun AnalysisCard(item: GoldAnalysisItem) {
    val cardBackground = Color(0xff1c1b1a)
    val iconBackground = Color(0xFF454545)
    val adviceColor = when (item.advice.action) {
        "BUY" -> Color(0xFF27ae60)
        "SELL" -> Color(0xFFe74c3c)
        else -> Color(0xFFf1c40f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardBackground)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Header: Title + Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = WhiteText
                )
                Image(
                    painter = painterResource(
                        id = if (item.title.contains("سکه")) R.drawable.ic_coin else R.drawable.ic_gold
                    ),
                    contentDescription = item.title,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(iconBackground)
                        .padding(8.dp)
                )
            }

            // Advice
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(adviceColor.copy(alpha = 0.2f))
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = item.advice.title,
                        fontWeight = FontWeight.Bold,
                        color = adviceColor,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = item.advice.description,
                        fontSize = 14.sp,
                        color = WhiteText.copy(alpha = 0.8f)
                    )
                }
            }

            // Market stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "قیمت زمان تحلیل: ${item.market_stats?.current_price}",
                    color = WhiteText,
                    fontSize = 14.sp
                )
                Text(
                    text = "تغییر: ${item.market_stats?.change_percent}",
                    color = adviceColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )
            }

            // Chart mini line
            if (item.chart_points.isNotEmpty()) {
                MiniLineChart(
                    points = item.chart_points,
                    lineColor = GoldText
                )
            }

        }
    }
}

@Composable
fun MiniLineChart(
    points: List<Long>,
    lineColor: Color = GoldText
) {
    val max = points.maxOrNull()?.toFloat() ?: 1f
    val min = points.minOrNull()?.toFloat() ?: 0f
    val diff = max - min

    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp))
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
                color = lineColor,
                start = androidx.compose.ui.geometry.Offset(startX, startY),
                end = androidx.compose.ui.geometry.Offset(stopX, stopY),
                strokeWidth = 4f
            )
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
        modifier = modifier,
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = textColor,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}