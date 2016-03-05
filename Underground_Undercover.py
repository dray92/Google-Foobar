def choose(n, k):
    """
    A fast way to calculate binomial coefficients by Andrew Dalke (contrib).
    """
    if 0 <= k <= n:
        ntok = 1
        ktok = 1
        for t in xrange(1, min(k, n - k) + 1):
            ntok *= n
            ktok *= t
            n -= 1
        return ntok // ktok
    else:
        return 0

def g(n, k):
    return sum(
        choose(n, s)*s*(n - s) * sum(
            f(s, t) * f(n - s, k - t) for t in range(0, k + 1)) for s in range(1, n))/2

def h(n,k):
    sum = 0
    
    for s in range(1,n):
        subSum = 0
        for t in range(0,k+1):
            subSum += f(s, t) * f(n - s, k - t)
        
        subSum = choose(n, s)*s*(n - s) * subSum
        sum+= subSum
    
    return sum/2

F = {}
def f(n, k):
    if (n, k) in F:
        return F[n, k]
    
    N = n * (n - 1)/2
    
    if k is n - 1:
        return int(n ** (n-2))
    if k < n or k > N:
        return 0
    if k == N:
        return 1
    
    result = ((N - k + 1) * f(n, k - 1) + h(n, k - 1)) / k
    F[n, k] = result
    return result
    
def test():
    for N in range(2, 21):
        for K in range(N-1, N*(N-1)/2 + 1):
            print "N = " + str(N) + " , K = " + str(K) + " , num = " + str(f(N, K))
            
            
test()