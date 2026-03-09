package be.business.androidpractice.coroutines

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

/*
* What is a Coroutine?

A coroutine is Kotlin’s way of writing asynchronous, non-blocking code that looks synchronous.

Think of coroutines as lightweight threads — thousands of them can run on a few actual threads without blocking.
* */
object BasicCoroutines {

    // Suspend function
    // The most basic building block of coroutines is the suspending function. It allows a running operation to pause and resume later without affecting the structure of your code.
    suspend fun generateNumber() {
        Log.d("BasicCoroutines", "Numbers:")
        for (i in 1..10) {
            Log.d("BasicCoroutines", "Number: $i")
            delay(2000)
        }
    }
    suspend fun greet() {
        delay(1000)
        println("The greet() on the thread: ${Thread.currentThread().name}")

    }
}
/* To Run a coroutine:
* A suspending function.
- A coroutine scope in which it can run, for example inside the withContext() function.
- A coroutine builder like CoroutineScope.launch() to start it.
- A dispatcher to control which threads it uses.*/
suspend fun coroutines() {
    // Coroutine Scope functions
    //There are four main coroutine scope functions that create or work with coroutine scopes:
    // 1. coroutineScope {}
    // 2. supervisorScope {}
    // 3. withContext(context)
    // 4. runBlocking {}

    // Switch context (e.g. from Main → IO) and return result
    withContext(Dispatchers.Default) {
        launch {
            BasicCoroutines.generateNumber()
        }
        launch { BasicCoroutines.greet() }
    }
    // If that context doesn't have a specified dispatcher, CoroutineScope.launch() uses Dispatchers.Default
    // Root of the coroutine subtree
    // Run multiple child coroutines and wait for all to finish
    coroutineScope { // this: CoroutineScope
        this.launch {
            this.launch {
                delay(2.seconds)
                println("Child of the enclosing coroutine completed")
            }
            println("Child coroutine 1 completed")
        }
        this.launch {
            delay(1.seconds)
            println("Child coroutine 2 completed")
        }
    }
    // Runs only after all children in the coroutineScope have completed
    println("Coroutine scope completed")



}
// Run child coroutines independently (one fails, others continue)
suspend fun supervisorScopeFetchAll() = supervisorScope {
    launch {
        try {
            fetchUser() // this will fail
        }catch (e: Exception){
            println(e.message)
        }

    }
    launch {
        fetchPosts() // this will still run
    }
    println("fetchAll() still continues after launch calls")
}

suspend fun fetchUser() {
    delay(500)
    println("Fetching user...")
    throw RuntimeException("User fetch failed ❌")
}

suspend fun fetchPosts() {
    delay(1000)
    println("Fetching posts... ✅")
}





