package com.jcb.moviesapp.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jcb.moviesapp.presentation.ui.movies.details.MovieDetailsScreen
import com.jcb.moviesapp.presentation.ui.movies.list.MoviesScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Any = Movies,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Movies> {
            MoviesScreen(navController = navController)
        }
        composable<MovieDetails> {
            val movieId = it.toRoute<MovieDetails>().moveId
            MovieDetailsScreen(movieId = movieId, navController = navController)
        }
    }
}
