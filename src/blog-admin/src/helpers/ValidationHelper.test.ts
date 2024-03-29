import * as enzyme from "enzyme";
import * as Adapter from "enzyme-adapter-react-16";
import { Validation, ValidationState } from "./ValidationHelper";

enzyme.configure({ adapter: new Adapter() });

it("should test notEmpty validation", () => {
    expect(
        Validation.validation("")
            .notEmpty()
            .toString()
    ).toEqual(ValidationState.Error);
    expect(Validation.notEmpty("").toString()).toEqual(ValidationState.Error);
    expect(Validation.notEmpty(" ").toString()).toEqual(ValidationState.Error);
    expect(Validation.notEmpty("a").toString()).toEqual(ValidationState.Success);
    expect(Validation.notEmpty(10).toString()).toEqual(ValidationState.Success);
    expect(Validation.notEmpty(0).toString()).toEqual(ValidationState.Success);
    expect(Validation.notEmpty(undefined).toString()).toEqual(ValidationState.Error);
});

it("should test between validation", () => {
    expect(
        Validation.validation(10)
            .between(5, 15)
            .toString()
    ).toEqual(ValidationState.Success);
    expect(Validation.between(10, 5, 15).toString()).toEqual(ValidationState.Success);
    expect(Validation.between(10, 10, 15).toString()).toEqual(ValidationState.Error);
    expect(Validation.between(10, 5, 10).toString()).toEqual(ValidationState.Error);
    expect(Validation.between("", 5, 10).toString()).toEqual(ValidationState.Error);
});

it("should test email validation", () => {
    expect(
        Validation.validation("a@a")
            .email()
            .toString()
    ).toEqual(ValidationState.Error);
    expect(Validation.email("a@a").toString()).toEqual(ValidationState.Error);
    expect(Validation.email("a").toString()).toEqual(ValidationState.Error);
    expect(Validation.email("a@example.com").toString()).toEqual(ValidationState.Success);
});

it("should test multiple validations", () => {
    expect(
        Validation.notEmpty("a@a")
            .email()
            .toString()
    ).toEqual(ValidationState.Error);
    expect(
        Validation.notEmpty("a@example.com")
            .email()
            .toString()
    ).toEqual(ValidationState.Success);
    expect(
        Validation.email("")
            .notEmpty()
            .toString()
    ).toEqual(ValidationState.Error);
    expect(
        Validation.notEmpty(10)
            .between(0, 15)
            .toString()
    ).toEqual(ValidationState.Success);
});

it("should test SameAs validation", () => {
    expect(Validation.sameAs("aaa", "aaa").toString()).toEqual(ValidationState.Success);
    expect(Validation.sameAs("aaa", "bbb").toString()).toEqual(ValidationState.Error);
    expect(
        Validation.notEmpty("aaa")
            .sameAs("aaa")
            .toString()
    ).toEqual(ValidationState.Success);
});

it("should test GraterThan validation", () => {
    expect(Validation.graterThan("", 0).toString()).toEqual(ValidationState.Error);
    expect(Validation.graterThan("a", 0).toString()).toEqual(ValidationState.Error);
    expect(Validation.graterThan(0, 0).toString()).toEqual(ValidationState.Error);
    expect(Validation.graterThan(-1, 0).toString()).toEqual(ValidationState.Error);
    expect(Validation.graterThan(undefined, 0).toString()).toEqual(ValidationState.Error);
    expect(Validation.graterThan(-2, -1).toString()).toEqual(ValidationState.Error);
    expect(Validation.graterThan(1, -1).toString()).toEqual(ValidationState.Success);
    expect(Validation.graterThan(-1, -2).toString()).toEqual(ValidationState.Success);
    expect(Validation.graterThan(1, 0).toString()).toEqual(ValidationState.Success);
});
