# No Man's Sky Crafting Tree Application<br/>
**Description**:<br/>
A tool that helps players navigate the crafting tree in No Man’s Sky by showing required ingredients and dependencies.
It also allows players to make their own crafting trees and view statistics on them.
This will be very useful for No Man's Sky players because that game features a very complex crafting tree for certain items and there is
often a lot of different steps to take (unlocking blueprints, refineries, etc.) to craft these items. As a player of No Man's Sky this project is of
great interest for me because whenever I play I often have to do a lot of searching online to find what I need to craft certain items. This application
will let me find what I need while I play the game much more easily.

**User Stories**: <br/>
- As a user, I want to be able to add new items (class X) to a crafting tree (class Y) as I unlock them.
- As a user, I want to be able to view the entire crafting tree for a specific item visually
- As a user, I want to be able to create, delete and modify several different trees.<br/>
- As a user, I want to have the program display the list of raw materials of the sub-components and/or associated costs required to make the item.
- As a user, I want to be able to have the option to save my trees and be reminded to save when quitting to the main menu
- As a user, I want to be able to load my saved trees upon reopening the application

**Phase 4: Task 2: Event Log Example** <br/>
Set saved trees to a new list <br/>
Added Table node to saved trees <br/>
Set Table node to selected <br/>
Set Table node to root <br/>
Set Table node to selected <br/>
Set Table node to selected <br/>
Set parent to Table <br/>
Added child Wood to parent Table <br/>
Set Wood node to selected <br/>
Set Table node to selected <br/>
Set parent to Table <br/>
Added child Screw to parent Table <br/>
Set Screw node to selected <br/>
Set Screw node to selected <br/>
Removed Screw from parent Table

**Phase 4: Task 3: What I Would do Better**: <br/>
If I had more time, I would refactor the Node and Treemanager classes to reduce coupling. Although the Treemanager class is useful to manage active nodes and the saved trees, there are many functions in TreeManager that's only purpose is to call Node functions for specific nodes. One way I could fix this is by finding the similar functions and putting them only in one class. That would allow the coupling to reduce while still trying to keep the one responsibility principle in mind. 

Also the Item class in is current state is pretty basic, mostly containing simple getters and setters. It could help to have Item take on a couple of the responsibilities of Node relating to Item since Node currently handles a lot of responsibilities. Some of Node's responsibilities can also be taken over by a few new subclasses to increase cohesion (for example: a class that searches a tree in different ways and another class that modifies the tree). 
