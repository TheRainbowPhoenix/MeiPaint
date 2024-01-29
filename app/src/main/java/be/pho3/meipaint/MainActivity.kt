package be.pho3.meipaint

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import be.pho3.meipaint.ui.theme.MeiPaintTheme
import com.medibang.android.paint.tablet.ui.activity.PaintActivity
import kotlinx.coroutines.launch


/*
class EntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
    }
    fun startJetpackComposeDraw(view: View) {
        startActivity(Intent(this, JcDrawActivity::class.java))
    }
    fun startNormalDraw(view: View) {
        startActivity(Intent(this, DrawActivity::class.java))
    }
}

 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeiPaintTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainActivityContent()
                }
            }
        }
    }
}

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Profile : Screen("profile", "Profile")
    object Settings : Screen("settings", "Settings")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityContent() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    LocalContext.current.applicationContext

    val drawerItemList = prepareNavigationDrawerItems()
    val selectedItem = remember { mutableStateOf(drawerItemList[0]) }

    val context = LocalContext.current
//    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                drawerItemList.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = null) },
                        label = { Text(text = item.label) },
                        selected = (item == selectedItem.value),
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            selectedItem.value = item
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }) {

        Scaffold(
            topBar = {
                MyTopAppBar {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(onNavIconClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Mei Paint") },
        navigationIcon = {
            IconButton(
                onClick = {
                    onNavIconClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open Navigation Items"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
        )
    )
}

private fun prepareNavigationDrawerItems(): List<NavigationDrawerData> {
    val drawerItemsList = arrayListOf<NavigationDrawerData>()

    // add items
    drawerItemsList.add(NavigationDrawerData(label = "Home", icon = Icons.Filled.Home))
    drawerItemsList.add(NavigationDrawerData(label = "feedbacks", icon = Icons.Filled.Email))
    drawerItemsList.add(NavigationDrawerData(label = "Evaluate", icon = Icons.Filled.Star))
    drawerItemsList.add(NavigationDrawerData(label = "About this App", icon = Icons.Filled.ThumbUp))
    drawerItemsList.add(NavigationDrawerData(label = "Profile", icon = Icons.Filled.Person))
    drawerItemsList.add(NavigationDrawerData(label = "Settings", icon = Icons.Filled.Settings))

    return drawerItemsList
}

data class NavigationDrawerData(val label: String, val icon: ImageVector)



@Composable
fun MainScreen() {
    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Section(title = "Let's draw!", options = listOf("New Canvas", "Previous", "My Gallery"), onClick = { option ->
            when (option) {
                "New Canvas", "Previous" -> {
                    navigateToPaintActivity(context)
                    // Handle navigation to PaintActivity
                } else -> {
                    otherActions(option, context)
                }
            }
        })
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Mei library", style = MaterialTheme.typography.headlineLarge, color = Color.White)
        Button(onClick = { navigateToPaintActivity(context) }) {
            Text(text="New Drawing")
        }
    }
}

@Composable
fun Section(title: String, options: List<String>, onClick: (String) -> Unit) {
    Text(text = title, style = MaterialTheme.typography.headlineLarge, color = Color.White)
    Spacer(modifier = Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
        options.forEach { option ->
            val iconResId = when (option) {
                "New Canvas" -> R.drawable.ic_edit
                "Previous" -> R.drawable.ic_edit_square
                else -> R.drawable.ic_menu_gallery
            }
            OptionCard(option, onClick, iconResId = iconResId)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionCard(option: String, onClick: (String) -> Unit, iconResId: Int? = null) {
    Column(
        modifier = Modifier.height(192.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(8.dp)
                .height(128.dp)
                .width(128.dp),
            onClick = { onClick(option) },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)

        ) {
            Icon(
                painter = painterResource(id = iconResId ?: R.drawable.ic_menu_gallery),
                contentDescription = option,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .align(Alignment.CenterHorizontally)
                    .size(64.dp)
            )

        }

        Spacer(modifier = Modifier.width(8.dp))
        Text(text = option, modifier = Modifier.weight(1f), color = Color.White)
    }


}

fun navigateToPaintActivity (context: Context) {
    val intent = Intent(context, PaintActivity::class.java)
    context.startActivity(intent)
}
fun otherActions (option: String, context: Context) {
    Log.d("MeiPaint::MainActivity", "option: $option")
}