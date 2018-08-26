# Scrapchemy
A text game using databases, done as a Java and SQLite exercise

Description of how it works are in /Scrapchemy/about.txt

current status:
- connection with database is working
- creating random Magical Item and its Magical Item Components from data taken from database is working
- game allows buying and selling generated Magical Items, updating the wallet successfully
- game progress can be saved to a .db file

to do in nearest future:
- disassembling Magical Items and processing Magical Item Components
- test if generated Magical Product commissions work properly and bring them into the game
- create the method to fulfill the commissions and count points
- loading the saved .db file to resume the game (methods to recreate all the objects from data in save file)

to do later:
- introduce multithreading into the program for timer-based actions like:
  - refreshing market - adding new items for sale at the market and deleting older ones
  - adding new commissions to fulfill
  - optionally, if the amount of money the player makes is too big to loose the game: charges - methods that will after certain periods of time take some amount of Player's money, described in the game as bills, charges or fees
