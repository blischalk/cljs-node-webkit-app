# Node Webkit Breakout

An example application to exercise the canvas, Node Webkit, and ClojureScript.

This application started out in a learning exercise in canvas, Node Webkit,
and ClojureScript but has expanded beyond that.  Im now more focused on how
generally stateful applications can be made stateless.  This Breakout game
was initially developed following a tutorial on canvas and porting to ClojureScript.
Now I am in the process of removing as much state as possible.  (Hopefully I
will be able to remove it all).

This application is also an experiment in Event Driven Programming.  I am making
a concious effort for things to not know about each other directly and communicate
via events.

## To Build A Release

`lein node-webkit-build`

The release will be placed in the releases directory.

## Development Workflow

Install [node-webkit](https://github.com/rogerwang/node-webkit)
somewhere on your path like ~/bin


I have created an alias in my .bash_profile like the following:

`export NODE_WEBKIT_HOME="$HOME/bin/node-webkit-v0.11.1-osx-x64/node-webkit.app/Contents/MacOS"`

`alias nodewebkit='$NODE_WEBKIT_HOME/node-webkit'`

From the root of the project run `lein cljsbuild auto`

This will have the project recompile the javascript every time 
the ClojureScript changes.

Next, change directory into the resources/public directory.

`cd resources/public`

From there, if you have created an alias like mine you can run:

`nodewebkit .`


This will start up the project.


