import { assertNever } from "./AssertNever";

export enum ValidationState {
    Success = "success",
    Error = "error"
    // Warning = 'warning'
}

export class Validation {
    private readonly validations: Array<() => boolean> = [];

    private readonly value: number | string | boolean | undefined;

    public static validation(value: number | string | boolean | undefined): Validation {
        return new Validation(value);
    }

    public static notEmpty(value: number | string | boolean | undefined): Validation {
        return new Validation(value).notEmpty();
    }

    public static email(value: number | string | boolean | undefined): Validation {
        return new Validation(value).email();
    }

    public static between(value: number | string | boolean | undefined, min: number, max: number): Validation {
        return new Validation(value).between(min, max);
    }

    public static sameAs(value: number | string | boolean | undefined, same: number | string | boolean | undefined) {
        return new Validation(value).sameAs(same);
    }

    public static graterThan(value: number | string | boolean | undefined, min: number) {
        return new Validation(value).graterThan(min);
    }

    notEmpty(): Validation {
        this.validations.push(() => {
            return !(
                typeof this.value === "undefined" ||
                typeof this.value === null ||
                (typeof this.value === "string" && this.value.trim().length === 0)
            );
        });
        return this;
    }

    email(): Validation {
        this.validations.push(() => {
            // http://emailregex.com/
            if (typeof this.value === "string") {
                return (
                    typeof this.value === "string" &&
                    // tslint:disable-next-line:max-line-length
                    /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(
                        this.value
                    )
                );
            }
            return false;
        });
        return this;
    }

    between(min: number, max: number): Validation {
        this.validations.push(() => {
            return this.value !== undefined && (this.value > min && this.value < max);
        });
        return this;
    }

    sameAs(sameAs: string | number | boolean | undefined): Validation {
        this.validations.push(() => {
            return this.value === sameAs;
        });
        return this;
    }

    graterThan(min: number): Validation {
        this.validations.push(() => {
            return this.value !== undefined && this.value > min;
        });
        return this;
    }

    validate(): ValidationState {
        for (let i = 0; i < this.validations.length; i++) {
            if (!this.validations[i]()) {
                return ValidationState.Error;
            }
        }
        return ValidationState.Success;
    }

    toString(): ValidationState {
        return this.validate();
    }

    isValid(): boolean {
        const validationResult = this.validate();
        switch (validationResult) {
            case ValidationState.Success:
                return true;
            case ValidationState.Error:
                return false;
            default:
                return assertNever(validationResult);
        }
    }

    private constructor(value: number | string | boolean | undefined) {
        this.value = value;
    }
}
