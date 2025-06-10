new

added new judy textures for floor tiles (thank you dear)



6/6/2025

ADDED GAME SPEED CONTROLS!! 
  
    added 3 speed settings: 4 frames per second (equal to the game speed before the others were added), 10
    frames per second, and 20 frames per second

    change speed settings with 1, 2, and 3, and toggle pause with space, 0, or ` (the key to the left of 1)

    sprites on the top left to indicate which setting is currently active or if the game is paused
        
        each sprite has an active and inactive version

        sprites are currently on a 40x40 canvas but this can be changed easily

organized resources folder

    floor tiles, meebs, ground objects, and ui elements each have their own folder and animated tiles each
    a subfolder beyond that (grass is the only animated tile currently)

    made the higher contrast grass the default

    sprites made by judy have _j added to the end (short for judy my lovely perfect beautiful artist
    girlfriend, which would've cluttered the file names)

meebs will cut trees and mine rocks infinitely (they have nothing better to do)

tree spawn likelihood decreases as the number of trees increases; the chance for a tree to spawn on any given
tile each frame is equal to: 1/(3000 + x^2), where x is the number of trees on the island at the end of the 
previous frame

reworked stone mining: in order to collect stone from a rock, a meeb must stand next to it with the mining
task active for 10 frames, after which 1 stone will be added to storage and the meeb will wander for a few
frames as a little break