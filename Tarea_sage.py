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

# 6

p = 0xffffffff00000001000000000000000000000000ffffffffffffffffffffffff
a = -3
b = 0x5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b
E = EllipticCurve (GF(p),[a,b])
G = E(0x6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296,\
      0x4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5)

Q = E(0xbbb041abbc4b2e9973726520c0a47b9db7d7a8d4e534f5c75d58acd68bf413a1,\
      0xc567543991c0d7aac45ac9c325f10b62c77189e3f0a34d977a448bde60590e2d)

C1 = E(0x26efcebd0ee9e34a669187e18b3a9122b2f733945b649cc9f9f921e9f9dad812,\
       0x90238bde9cc7bb330d150c67704dd25ae7055205744b6f31bf4070745872d0e6)
C2 = E(0x46c4b5cc528f15953943e0a775d3a6d08057b1fa30878473aaa28399198c4f8c,\
       0x3edff2f73f7a89cfddc967ad9af2f36a06b690761a5d25810efe223f906ce63)

# Llave secreta
Z = G
Sk = 1
while Z != Q:
  Z += G
  Sk +=1
print ('0x%02x' % Sk) # 0xa2d77, 666999

# Mensaje
M = C2 - Sk*C1
print ('(0x%02x,0x%02x)' % (M[0],M[1]))
# (0x747d0fbd48003c15cec4320a14bdcf047fbec6717b3b21ae9e2fbee94f90fcb7,
#  0x60ccfb4f29bad1287c1429b5384591ad39cdcb6445c79e4956a7008e381fab14)
# (52689254947963902595739054002869248928648273535722072553023360335275059379383,
#  43784204737771850847989304628113050818591064012305593450095436451846828305172)

# 7

# $ openssl rsa -pubin -inform PEM -text -noout < public.key
pk1 = (0x00ace5e558204976ff266da8aa43de450c2da661a52e725336b7b82cac77554963b4bcd85a3e31a06f9a1c1a2ec094fb0f27a26e96ac087f1875eaebe332064cd5,65537)
pk2 = (0x00fb2ba70c79e8e4e52ad1805a7db86e8e47520df162d9f39d38f55fff07abba4b60d215139ed8c68cabdf34ce38126e7e04cdcdd292d117394e2e33654902910be9,65537)

# N1 y N2 tienen un factor en común que podemos obtener eficientemente con el
# algoritmo de Euclides, enctonces calcularmos eficientemente phi(N1) y d
# mediante el algoritmo extendido de Euclides.
p = gcd (pk1[0],pk2[0])
q = pk1[0]/p
phi = (p-1)*(q-1)
bezout = xgcd(pk1[1],phi)
d = Integer(mod(bezout[1], phi))
# Ya con d desciframos c y obtenemos m
c = 0x41b4e1609390ff8fb5f225b010d1cc79253dcab1744d5f865daabad0e28d259141722382114d9a73106b4d429676dae60a1528a0eb3b73eab0e9d2165c72492f
m = power_mod (c, d, pk1[0]) # 195894762
print ('0x%02x' % m)
