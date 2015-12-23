var x = IN.x();
var y = IN.y();
var a = IN.angle();

if (a < 0) {
    OUT.chassis_left(50);
    OUT.chassis_right(-50);
} else {
    OUT.chassis_left(-50);
    OUT.chassis_right(50);
}
OUT.scan(a);
OUT.fire();

this.s = "" + this.s + "1";
print(this.s);

function tetest() {
    fail();
}

//tetest();
