#!/bin/sh
montage `ls roads-*.png` -geometry 50x50 -tile 5x16 -background '#ffffff' tileset-roads.png
montage `ls borders-*.png` -geometry 50x50 -tile 5x4 -background '#00ff00' tileset-borders.png
montage `ls walls-*.png` -geometry 50x50 -tile 5x20 -background '#00ff00' tileset-walls.png
montage `ls doors-*.png` -geometry 50x50 -tile 5x20 -background '#ffff00' tileset-doors.png
montage `ls roofs-*.png` -geometry 50x50 -tile 5x20 -background '#000000' _tileset-roofs.png
convert _tileset-roofs.png -fill blue -tint 40 tileset-roofs.png
convert objects-01-robot.png -fill red -tint 40 objects-02-redrobot.png
convert objects-01-robot.png -fill blue -tint 40 objects-03-bluerobot.png
montage `ls objects-*.png` -geometry 50x50 -tile 5x20 -background '#ff00ff' tileset-objects.png
