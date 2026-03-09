package be.business.androidpractice.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

/*
* Coroutine builder functions
- A coroutine builder function is a function that accepts a suspend lambda that defines a coroutine to run. Here are some examples:
- CoroutineScope.launch()
- CoroutineScope.async()
- runBlocking()
- withContext()
- coroutineScope()

Coroutine builder functions require a CoroutineScope to run in. This can be an existing scope or one you create with helper functions such as coroutineScope(), runBlocking(), or withContext(). Each builder defines how the coroutine starts and how you interact with its result.*/

// 1. Launch Builder function
suspend fun CoroutineScope.launchAsBuilder(){
    // Starts a coroutine that runs without blocking the scope
    this.launch {
        delay(100.milliseconds)
        println("Sending notification in background")
    }
    println("Scope continues")
}
// 2. CoroutineScope.async
// The CoroutineScope.async() coroutine builder function is an extension function on CoroutineScope.
// It starts a concurrent computation inside an existing coroutine scope and returns a Deferred handle that represents an eventual result.
// Use the .await() function to suspend the code until the result is ready

suspend fun CoroutineScope.asyncAsBuilder() = withContext(Dispatchers.Default) {
    val firstPage = async {
        delay(50.milliseconds)
        "First page"
    }

    val secondPage = async {
        delay(100.milliseconds)
        "Second page"
    }

    val pagesAreEqual = firstPage.await() == secondPage.await()
    println(firstPage.await())
    println("Pages are equal: $pagesAreEqual")
}
// 3. runBlocking()
//The runBlocking() coroutine builder function creates a coroutine scope and blocks the current thread until the coroutines launched in that scope finish.
//Use runBlocking() only when there is no other option to call suspending code from non-suspending code:
fun runBlockingBuilder(){

}
// A third-party interface you can't change
interface Repository {
    fun readItem(): Int
}

object MyRepository : Repository {
    override fun readItem(): Int {
        // Bridges to a suspending function
        // We can't call a suspending function in a non suspending function without the use of suspend
        // To bridge them we used run Blocking.  Note: Its not safe because it will block the current thread
        return runBlocking {
            myReadItem()
        }
    }
}

suspend fun myReadItem(): Int {
    delay(100.milliseconds)
    return 4
}
