package com.codebyedu.sample1.ui.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codebyedu.sample1.R
import com.codebyedu.sample1.ui.compose.theme.CodebyEduTheme
import com.codebyedu.sample1.ui.fragments.WinActivity
import kotlin.math.roundToInt
import kotlin.random.Random

class GameActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rnd = Random(435) // для случайного положения кактуса
        val userName = intent.getStringExtra(USER_NAME) // имя пользователя из Интента
        setContent {
            CodebyEduTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Game(userName!!, rnd, WIN_SCORE)  // функция Игры
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context, userName: String): Intent =
            Intent(context, GameActivity::class.java).apply {
                putExtra(USER_NAME, userName)
            }

        private const val USER_NAME = "com.codebyedu.sample1.ui.compose.GameActivity.user"
        private const val WIN_SCORE = 15
    }
}

val DINO_WIDTH = 50.dp
val CACTUS_WIDTH = 30.dp

@Composable
fun Game(name: String, rnd: Random, winScore: Int) {

    val scores = remember {
        mutableIntStateOf(-1)
    }

    //контейнер-столбец (аналог LinearLayout в вертикальной ориентации)
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.padding(16.dp)) // отступ
        Text(text = "Hello, $name", fontSize = 32.sp) // виджет с текстом приветсвия
        Spacer(modifier = Modifier.padding(16.dp)) // отступ
        Text(text = "scores: ${scores.intValue}")  // виджет с набранными очками

        if (scores.intValue < winScore) { // если очки не достигли порога победы - играем
            GameScreen(rnd) { newScore -> scores.intValue = scores.intValue + newScore }
        } else { // если очки превысили порог победы, то игра заканчивается
            FinishGameScreen(name)
        }
    }
}

@Composable
fun GameScreen(rnd: Random, onScoreChanged: (Int) -> Unit) {
    // сдвиг Дино
    val offsetX = remember { mutableFloatStateOf(0f) }
    // размер Дино в пикселях
    val dinoWidth = with(LocalDensity.current) { DINO_WIDTH.toPx() }
    // размер кактуса в пикселях
    val cactusWidth = with(LocalDensity.current) { CACTUS_WIDTH.toPx() }
    // объект-состояния с очками пользователя
    // размер игрового поля
    val size = remember {
        mutableStateOf(IntSize.Zero)
    }
    // сдвиг кактуса
    val offsetXCactus = remember { mutableIntStateOf(size.value.width.div(2)) }
    // направление движения Дино (true - если направо)
    val directionRight = remember {
        mutableStateOf(true)
    }
    Box(
        Modifier
            .fillMaxSize()
            .onSizeChanged {
                size.value = it
                offsetXCactus.intValue = it.width / 2
            },
        contentAlignment = Alignment.BottomStart
    ) {
        // виджет с картинкой Дино
        Image(
            painter = painterResource(
                if (directionRight.value) R.drawable.ic_dino
                else R.drawable.ic_dino_left
            ),
            contentDescription = "dino",
            modifier = Modifier
                .pointerInput(Unit) {
                    // здесь обрабатывается перетаскивание Дино
                    detectDragGestures(onDragEnd = {
                        offsetX.floatValue = 0f
                    }) { change, dragAmount ->
                        val newDinoX = offsetX.floatValue + dragAmount.x
                        val rightBorder = size.value.width - dinoWidth
                        offsetX.floatValue = when {
                            newDinoX < 0 -> 0f
                            newDinoX > rightBorder -> size.value.width - dinoWidth
                            else -> newDinoX
                        }
                        directionRight.value = dragAmount.x > 0
                        change.consume()
                    }
                }
                .size(DINO_WIDTH)
                .offset { IntOffset(offsetX.floatValue.roundToInt(), 0) }
        )
        val dinoCenter = offsetX.floatValue + dinoWidth / 2
        // проверка на совпадение координат Дино и кактуса и увеличение очков
        if (dinoCenter > offsetXCactus.intValue && dinoCenter < offsetXCactus.intValue + cactusWidth) {
            onScoreChanged(1)
            val r = size.value.width - cactusWidth.roundToInt()
            // новое положение кактуса
            if (r > 0) offsetXCactus.intValue = rnd.nextInt(0, r)
        } else {
            // виджет с картинкой кактуса
            Image(painter = painterResource(id = R.drawable.ic_cactus),
                contentDescription = "cactus",
                modifier = Modifier
                    .offset { IntOffset(offsetXCactus.intValue, 0) }
                    .size(CACTUS_WIDTH))
        }
    }
}

@Composable
fun FinishGameScreen(userName: String) {
    Text(text = "GAME OVER", fontSize = 32.sp, color = Color.Red)
    val context = LocalContext.current
    Button(onClick = { startWinActivity(context, userName) }) {
        Text(text = "OK")
    }
}

private fun startWinActivity(context: Context, userName: String) {
    context.startActivity(WinActivity.newIntent(context, userName))
    (context as ComponentActivity).finish()
}

// функция ниже позвоялет разработчику увидеть как будет выглядеть UI
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CodebyEduTheme {
        Game("Android", Random(333), 15)
    }
}