

from alias import Real as Alias
from alias import Real
from alias import IndirectAlias as AliasAlias

r = Real("str")
r.just_a_method(1)

a = Alias(1)
a.just_a_method(1.1)

aa = AliasAlias("")

aa.a_method("xxx")