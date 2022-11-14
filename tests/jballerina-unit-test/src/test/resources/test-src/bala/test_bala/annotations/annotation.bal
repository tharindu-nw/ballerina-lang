import testorg/foo;

@foo:ConfigAnnotation {
    numVal: 10,
    textVal: "text",
    conditionVal: false,
    recordVal: { nestNumVal: 20, nextTextVal: "nestText" }
}
function someFunction(string arg) returns int {
    return 10;
}

function testNonBallerinaAnnotations() returns foo:SomeConfiguration? {
    var tDesc = typeof someFunction;
    return tDesc.@foo:ConfigAnnotation;
}

function testAnnotOnBoundMethod() {
    foo:MyClass c = new;
    typedesc t1 = typeof c.foo;
    assertEquality((), t1.@foo:ObjMethodAnnot);

    typedesc t2 = typeof c.bar;
    foo:OtherConfiguration? r = t2.@foo:ObjMethodAnnot;
    assertEquality(true, r is foo:OtherConfiguration);
    foo:OtherConfiguration rec = <foo:OtherConfiguration> r;
    assertEquality(102, rec.i);
}

type AnnotationRecord record {|
    int minValue;
    int maxValue;
|};

annotation AnnotationRecord BarAnnotation on type;
annotation AnnotationRecord BarAnnotation2 on type;

@BarAnnotation {
    minValue: 18,
    maxValue: 36
}
type Bar int|float;
type Bar2 int|float;

@BarAnnotation2 {
    minValue: 18,
    maxValue: 36
}
type Bar3 Bar4;
type Bar4 Bar2;
type Bar5 Bar;

function testAnnotationsOnTypeRefTypes() {
    var f1 = function (typedesc td) returns AnnotationRecord? {
        return td.@BarAnnotation;
    };

    AnnotationRecord? 'annotation = f1(Bar);
    assertTrue('annotation is AnnotationRecord);
    AnnotationRecord rec = <AnnotationRecord> 'annotation;
    assertEquality(rec.minValue, 18);
    assertEquality(rec.maxValue, 36);

    'annotation = f1(Bar2);
    assertTrue('annotation is ());

    var f2 = function (typedesc td) returns AnnotationRecord? {
        return td.@BarAnnotation2;
    };

    'annotation = f2(Bar3);
    assertTrue('annotation is AnnotationRecord);
    rec = <AnnotationRecord> 'annotation;
    assertEquality(rec.minValue, 18);
    assertEquality(rec.maxValue, 36);

    'annotation = f2(Bar4);
    assertTrue('annotation is ());

    'annotation = f1(Bar5);
    assertTrue('annotation is ());

    'annotation = f2(Bar5);
    assertTrue('annotation is ());
}

function assertTrue(anydata actual) {
    assertEquality(true, actual);
}

function assertEquality(anydata expected, anydata actual) {
    if expected == actual {
        return;
    }

    panic error(string `expected ${expected.toBalString()}, found ${actual.toBalString()}`);
}
