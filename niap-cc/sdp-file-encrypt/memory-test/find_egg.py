import mmap

# Pattern to search for: 32 bytes of 0x48 0x04 repeated 16 times
pattern = bytes.fromhex("016917548121da2088452ff55655aaa1849f8dde81d12918d8bc71a45db2c299b82a6da1fffbdb900c40e6876b1dc7a84d9f1af14ed5b5809b43b0375effed9ddbe5")
filename = "main_ram_6gb.dump"


print(f"[*] Searching for the specific DEK pattern in {filename}...")

with open(filename, "rb") as f:
    # Memory-map the 6GB file (extremely fast and memory-efficient)
    mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)

    offset = 0
    count = 0
    while (offset := mm.find(pattern, offset)) != -1:
        count += 1
        print(f"[!] Found! Absolutfie offset: {offset} (Hex: 0x{offset:x})")
        offset += 1

print(f"[*] Search complete. Total {count} instances found.")