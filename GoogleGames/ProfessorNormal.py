f = open('input')
fw = open('output', 'w')

def getNeighborsCount(cI, cJ, mat, maxM, maxN):
	count = 0
	if (cI-1 >= 0 and mat[cI-1][cJ] >= 0):
		count+=1
	if (cI+1 < maxM and mat[cI+1][cJ] >= 0):
		count+=1
	if (cJ-1 >= 0 and mat[cI][cJ-1] >= 0):
		count+=1
	if (cJ+1 < maxN and mat[cI][cJ+1] >= 0):
		count+=1
	
	return count

def getNeighbors(cI, cJ, mat, maxM, maxN):
	toRet = []
	count = 0
	if (cI-1 >= 0 and mat[cI-1][cJ] >= 0):
                toRet.append((cI-1,cJ))
		count+=1
        print maxM
	if (cI+1 < maxM and mat[cI+1][cJ] >= 0):
                print count
		toRet.append((cI+1,cJ))
		count+=1
        if (cJ-1 >= 0 and mat[cI][cJ-1] >= 0):
                toRet.append((cI, cJ-1))
		count+=1
        if (cJ+1 < maxN and mat[cI][cJ+1] >= 0):
                toRet.append((cI, cJ+1))
		count+=1

	return toRet

# Get the number of test cases
numTests = f.readline()

for x in range(0, len(numTests)) :
	# Get the number of M
	M = int(f.readline())

	# Get the number of N
	N = int(f.readline())

	# Make a 2d array
	a = []

	for l in range(M):
		a.append([])

	# Temp array
	b = []

	for l in range(M):
		temp = []
		for k in range(N):
			temp.append(0)
		b.append(temp)
	
	for y in range(M):
		cols = f.readline()		
		colsSplit = cols.split()
		a[y] = colsSplit
	
	numExchanges = 0
	numChildren = M*N
	# One exchange
	for t in range(10000) :
		for i in range(M):
			for j in range(N):
				if (a[i][j] < 12 or getNeighborsCount(i, j, a, M, N) == 0):
					# Dis guy he out
					a[i][j] = -1
					numChildren-= 1
		if (numChildren == 0):
			break
		# Give out
		for i in range(M):
			for j in range(N):
				numToGive = 12/getNeighborsCount(i, j, a, M, N)
				neighborsCoordinate = getNeighbors(i, j, a, M, N)
				b[i][j] -= 12
				for z in range(len(neighborsCoordinate)):
					b[neighborsCoordinate[z][0]][neighborsCoordinate[z][1]] += numToGive
		numExchanges+= 1

	if (numExchanges < 10000):
		fw.write('Case #' + str(M) + ':  ' + str(numExchanges) + ' turns\n')
	else:
		fw.write('Case #' + str(M) + ':  ' + str(numChildren) + ' children will play forever') 
										


