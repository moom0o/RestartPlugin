# RestartPlugin
Restart notifications just like the minecraft server 2b2t

Completely configurable messages

```
# 2b2t restart plugin

# Set to 99999999 to restart exactly at your specified time
# If the players are not low for 5 days then the server will forcefully restart even if there is a ton of players online.
MinimumPlayersToRestart: 4

# Time to where the server will call a restart, if the server hasnt reached the minimum number of players to restart it will wait until the player count has been reached, then it will start a 15 minute countdown.
Timezone: "America/New_York"
#Time
# 2:00 AM EST
Hour: 2
Minute: 0
Seconds: 0

string: "&e[SERVER] Server restarting in"
finalstring: "&e[SERVER] Server restarting..."
minutestring: " minutes..."
secondsstring: " seconds..."
secondstring: " second..."
```