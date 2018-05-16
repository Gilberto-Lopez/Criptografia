# 1a
N = 35 # euler_phi(N) = 24
e = 5
d = inverse_mod(e,euler_phi(N)) # 5
R = Zmod(N)
c = R(10)
M = c^d # 5
print (M)

# 1b
N = 3599 # euler_phi(N) = 3480
e = 31
d = inverse_mod(e,euler_phi(N)) # 3031
print (d)

# 2
K = Zmod(71)
#Km = K.unit_group()
g = K(7)

# 2a
M = K(30)
YB = K(3)
k = 2
a = g^k # 49
b = YB^k * M # 57
print (a,b)

# 2b
a = K(59)
k = a.log(g)
C2 = YB^k * M # 29
print (C2)
