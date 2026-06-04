adb shell "dd if=/proc/kcore of=/data/local/tmp/main_ram_6gb.dump bs=4096 skip=8388611 count=1572864 conv=noerror,sync"
adb pull /data/local/tmp/main_ram_6gb.dump .
xxd -s $((0x1a2b3c40 - 64)) -l 256 main_ram_6gb.dump



xxd -s $((0x150036099 - 64)) -l 256 main_ram_6gb.dump
xxd -s $((0x150756700 - 64)) -l 256 main_ram_6gb.dump
xxd -s $((0x150756910 - 64)) -l 256 main_ram_6gb.dump
xxd -s $((0x166b9c09c - 64)) -l 256 main_ram_6gb.dump


xxd -s $((0x150757e1f - 64)) -l 256 main_ram_6gb.dump

xxd -s $((0x1507aa43f - 64)) -l 256 main_ram_6gb.dump

xxd -s $((0x1507aa27f - 64)) -l 256 main_ram_6gb.dump
xxd -s $((0x1507ad1c0- 64)) -l 256 main_ram_6gb.dump