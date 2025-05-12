
sealed interface Screen {
    data object Map: Screen
    data object Markers: Screen
}

class NavViewModel : ViewModel() {
    val currentScreen = mutableStateOf<Screen>(Screen.Map)
    fun navTo(screen: Screen) { currentScreen.value = screen }
}

@OptIn(InternalComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val viewModel = viewModel { NavViewModel() }
    val currentScreen = viewModel.currentScreen.value

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú")
                NavigationDrawerItem(
                    label = { Text("Map") },
                    selected = false,
                    icon = { Icon(Icons.Default.Place, contentDescription = "Map") },
                    onClick = {
                        viewModel.navTo(Screen.Map)
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Markers") },
                    selected = false,
                    icon = { Icon(Icons.Default.Search, contentDescription = "Markers") },
                    onClick = {
                        viewModel.navTo(Screen.Markers)
                        scope.launch { drawerState.close() }
                    }
                )
            }
        },
        drawerState = drawerState,
        gesturesEnabled = false
    ) {
        Scaffold(
            Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Menú") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) {
            when (currentScreen) {
                Screen.Map -> MapsScreen()
                is Screen.Markers -> MarkersScreen()
            }
        }
    }
}