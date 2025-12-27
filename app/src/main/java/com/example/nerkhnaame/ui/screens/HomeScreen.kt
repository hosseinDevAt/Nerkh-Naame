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
import com.example.nerkhnaame.ui.theme.BackViewBlack
import com.example.nerkhnaame.ui.theme.GoldText
import com.example.nerkhnaame.ui.theme.WhiteText
import com.example.nerkhnaame.viewModel.HomeState
import com.example.nerkhnaame.viewModel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun Home(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 1)
    val scope = rememberCoroutineScope()
    val currentPage by remember {
        derivedStateOf { pagerState.currentPage }
    }

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
                    text = state.todayDate,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = TextStyle(
                        textDirection = TextDirection.Rtl,
                        textAlign = TextAlign.Center
                    )
                )

                Text(
                    text = state.holiday,
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
                0 -> {}
                1 -> {
                    PriceListScreen(state)
                }
            }
        }

    }
}

@Composable
fun PriceListScreen(state: HomeState) {

    if (state.isLoading) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = GoldText)
        }

    }else if (state.error != null){

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(color = GoldText)
        }

    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(top = 16.dp)
        ) {

            itemsIndexed(state.golds) { index ,goldItem ->
                AnimatedPriceItem(index, goldItem)
            }

        }
    }

}
@Composable
fun ListPriceItem(
    gold: Gold
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
fun AnimatedPriceItem(index: Int, gold: Gold) {
    val visible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 50L)
        visible.value = true
    }

    AnimatedVisibility(
        visible = visible.value,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        modifier = Modifier.fillMaxWidth()
    ) {
        ListPriceItem(gold)
    }
}

@Composable
fun AnalysisScreen() {
    //TODO(analysis screen)
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