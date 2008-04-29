# some relativ path checks
from import_package.path_check import PathCheck
from import_package.sub_package.sub_sub_package.deep_module import DeepModule
from same_level_import import SameLevel as ExactlySameLevel

import same_level_import


pc = PathCheck()
o = pc.get_rel_os()()
o ## type Os

print o

foo = pc.get_abs_foo()()
foo ## type Foo

mod = DeepModule()
mod ## type DeepModule

ldeep = mod.less_deep()()
ldeep ## type LessDeep

x = ExactlySameLevel(2)
out = x.get_field()

# doesn't work because ExactlySameLevel is an alias for SameLevel and therefore isn't found by the PossbileReferences gang
out # # TODO type str

xx = same_level_import.SameLevel()
out2 = xx.get_field()
out2 # # TODO type str