#!/bin/sh
montage `ls roads-*.png` -geometry 50x50 -tile 5x -background '#ffffff' tileset-roads.png
montage `ls walls-*.png` -geometry 50x50 -tile 5x -background '#00ff00' tileset-walls.png
montage `ls doors-*.png` -geometry 50x50 -tile 5x -background '#ffff00' tileset-doors.png
montage `ls roofs-*.png` -geometry 50x50 -tile 5x -background '#000000' _tileset-roofs.png
convert _tileset-roofs.png -fill blue -tint 40 tileset-roofs.png
convert objects-01-robot.png -fill red -tint 40 objects-02-redrobot.png
convert objects-01-robot.png -fill blue -tint 40 objects-03-bluerobot.png
montage `ls objects-*.png` -geometry 50x50 -tile 5x -background '#ff00ff' tileset-objects.png
