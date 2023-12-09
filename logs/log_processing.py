def get_average(total_time, count):
    return (total_time / count) / 1000000

import sys

files = sys.argv[1:]
print(files)

ts, tj, count = 0, 0, 0
for file in files: 
    with open(file) as f:
        for line in f:
            times = line.split(' ')
            ts_nano_time, tj_nano_time = int(times[0]), int(times[1])
            ts += ts_nano_time
            tj += tj_nano_time
            count += 1

print('total ts:', ts, 'total tj:', tj, 'count:', count)

print('average TS:', get_average(ts, count))
print('average TJ:', get_average(tj, count))