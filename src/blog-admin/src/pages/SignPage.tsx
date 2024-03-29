import * as React from "react";
import { RouteComponentProps, Redirect } from "react-router";
import PageHelper from "../helpers/PageHelper";
import { RouteName } from "../Router";
import { State } from "../BlogAdminStore";
import SignIn from "../components/SignIn";
import SignUp from "../components/SignUp";
import LoadingOverlay from "../components/LoadingOverlay";
import { observer } from "mobx-react";
import { SignInAction, SignUpAction } from "../api/SignControllerApi";
import { Validation } from "../helpers/ValidationHelper";

interface SignPageParams {
    action: string;
}

interface PageProps extends RouteComponentProps<SignPageParams> {}

@observer
export default class SignPage extends React.Component<PageProps> {
    state: {
        errorMessage?: string;
    };

    constructor(props: PageProps) {
        super(props);
        this.state = {
            errorMessage: undefined
        };
    }

    render() {
        switch (this.props.match.params.action) {
            case "in":
                if (PageHelper.isUserSignedIn()) {
                    State.mainNavigation.redirectLink = RouteName.home;
                    return <Redirect push={true} to={RouteName.home} />;
                }
                return (
                    <div>
                        <SignIn
                            handleSignIn={(email, password) => {
                                this.onSignInClick(email, password);
                            }}
                            errorMessage={this.state.errorMessage}
                        />,
                        <LoadingOverlay display={State.isLoading} text="Signing in" />
                    </div>
                );
            case "up":
                if (PageHelper.isUserSignedIn()) {
                    return <Redirect push={true} to={RouteName.home} />;
                }
                return (
                    <div>
                        <SignUp
                            handleSignUp={(email, nickname, password, confirmPassword, name, surname) => {
                                this.onSignUpClick(email, nickname, password, confirmPassword, name, surname);
                            }}
                            errorMessage={this.state.errorMessage}
                        />,
                        <LoadingOverlay display={State.isLoading} text="Signing up" />
                    </div>
                );
            case "password":
                return false;
            default:
                return <Redirect push={true} to={RouteName.signIn} />;
        }
    }

    private onSignInClick(email: string, password: string): void {
        if (
            Validation.notEmpty(email)
                .email()
                .isValid() &&
            Validation.notEmpty(password).isValid()
        ) {
            SignInAction(email, password, error => {
                if (error !== undefined) {
                    return this.setState({ errorMessage: error });
                }
            });
        }
    }

    private onSignUpClick(
        email: string,
        nickname: string,
        password: string,
        confirmPassword: string,
        name: string,
        surname: string
    ) {
        if (
            Validation.notEmpty(email)
                .email()
                .isValid() &&
            Validation.notEmpty(nickname).isValid() &&
            Validation.notEmpty(password)
                .sameAs(confirmPassword)
                .isValid()
        ) {
            SignUpAction(email, nickname, password, name, surname, error => {
                if (error !== undefined) {
                    return this.setState({ errorMessage: error });
                }
            });
        }
    }
}
