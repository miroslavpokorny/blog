import * as React from 'react';
import * as enzyme from 'enzyme';
import * as Adapter from 'enzyme-adapter-react-16';
import { Validation, ValidationState } from './ValidationHelper';

enzyme.configure({ adapter: new Adapter() });

it('should test notEmpty validation', () => {
    expect(Validation.validation('').notEmpty().toString()).toEqual(ValidationState.Error);
    expect(Validation.notEmpty('').toString()).toEqual(ValidationState.Error);
    expect(Validation.notEmpty(' ').toString()).toEqual(ValidationState.Error);
    expect(Validation.notEmpty('a').toString()).toEqual(ValidationState.Success);
    expect(Validation.notEmpty(10).toString()).toEqual(ValidationState.Success);
});

it('should test between validation', () => {
    expect(Validation.validation(10).between(5, 15).toString()).toEqual(ValidationState.Success);
    expect(Validation.between(10, 5, 15).toString()).toEqual(ValidationState.Success);
    expect(Validation.between(10, 10, 15).toString()).toEqual(ValidationState.Error);
    expect(Validation.between(10, 5, 10).toString()).toEqual(ValidationState.Error);
    expect(Validation.between('', 5, 10).toString()).toEqual(ValidationState.Error);
});

it ('should test email validation', () => {
    expect(Validation.validation('a@a').email().toString()).toEqual(ValidationState.Error);
    expect(Validation.email('a@a').toString()).toEqual(ValidationState.Error);
    expect(Validation.email('a').toString()).toEqual(ValidationState.Error);
    expect(Validation.email('a@example.com').toString()).toEqual(ValidationState.Success);
});

it ('should test multiple validations', () => {
    expect(Validation.notEmpty('a@a').email().toString()).toEqual(ValidationState.Error);
    expect(Validation.notEmpty('a@example.com').email().toString()).toEqual(ValidationState.Success);
    expect(Validation.email('').notEmpty().toString()).toEqual(ValidationState.Error);
    expect(Validation.notEmpty(10).between(0, 15).toString()).toEqual(ValidationState.Success);
});