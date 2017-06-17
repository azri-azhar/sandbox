# Sandbox
Sandbox is a personal workspace to try out new ideas and store reusable Java code snippets. Sandbox components are not necessarily be maintained, or used in actual production.

# What's Available?
* Observable

  A simple implementation of observer pattern without boilerplate code. A listener is executed in a single worker thread operating off an unbounded queue, with a timeout value to cancel the execution. This is to prevent from one listener from blocking other listeners on the list. Listeners are stored in a "snapshot" style thread-safe array.
