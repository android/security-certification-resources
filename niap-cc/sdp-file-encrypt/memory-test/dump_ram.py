import subprocess
import struct
import sys

def extract_ram():
    print("[*] Parsing /proc/kcore ELF header and retrieving segment list...\n")
    # Extracting a bit more of the header (completes instantly)
    cmd = ["adb", "exec-out", "dd if=/proc/kcore bs=4096 count=4 2>/dev/null"]
    try:
        header = subprocess.check_output(cmd)
    except Exception as e:
        print(f"[!] Failed to execute adb: {e}")
        sys.exit(1)

    if header[:4] != b'\x7fELF':
        print("[!] Error: Not a valid ELF file.")
        sys.exit(1)

    e_phoff = struct.unpack_from("<Q", header, 0x20)[0]
    e_phentsize = struct.unpack_from("<H", header, 0x36)[0]
    e_phnum = struct.unpack_from("<H", header, 0x38)[0]

    print(f"{'Index':<5} | {'Offset':<18} | {'VirtAddr':<18} | {'Size (GB)':<10}")
    print("-" * 65)

    candidates = []

    # Scan and list all program headers
    for i in range(e_phnum):
        ph_start = e_phoff + (i * e_phentsize)
        p_type = struct.unpack_from("<I", header, ph_start)[0]

        if p_type == 1: # PT_LOAD (Loadable memory segment)
            p_offset = struct.unpack_from("<Q", header, ph_start + 0x08)[0]
            p_vaddr = struct.unpack_from("<Q", header, ph_start + 0x10)[0]
            p_filesz = struct.unpack_from("<Q", header, ph_start + 0x20)[0]

            size_gb = p_filesz / (1024**3)
            print(f"[{i:2d}]   | 0x{p_offset:016x} | 0x{p_vaddr:016x} | {size_gb:.2f} GB")

            # List realistic sizes (1GB - 16GB) for Shusky's RAM (around 12GB)
            if 1.0 < size_gb <= 16.0:
                candidates.append((i, p_offset, p_filesz, size_gb))

    print("-" * 65)

    if not candidates:
        print("\n[!] No segment with a realistic physical RAM size (1GB - 16GB) was found.")
        return

    print("\n[*] Segments highly likely to be physical RAM:")
    for c in candidates:
        print(f"  -> Index [{c[0]}]: Size {c[3]:.2f} GB (Offset: 0x{c[1]:x})")

    # Auto-select the one closest to 12GB (Shusky's RAM size)
    best_match = min(candidates, key=lambda x: abs(x[3] - 12.0))
    target_offset = best_match[1]
    largest_size = best_match[2]

    skip_blocks = target_offset // 4096
    count_blocks = largest_size // 4096
    out_file = "shusky_physical_ram.dump"

    print(f"\n[*] Command to extract the most likely candidate (Index [{best_match[0]}]):")
    ext_cmd = f"adb exec-out \"dd if=/proc/kcore bs=4096 skip={skip_blocks} count={count_blocks} 2>/dev/null\" > {out_file}"
    print(f"    {ext_cmd}")

    # print("\nCopy and run the command above, or start extraction automatically?")
    ans = input("[*] Press Enter to exit. ")
    # if ans.lower() == 'y':
    #    print(f"[*] Extracting... (Transferring actual data only, will complete in a few minutes)")
    #    subprocess.run(ext_cmd, shell=True)
    #    print("[*] Extraction complete!")

if __name__ == '__main__':
    extract_ram()