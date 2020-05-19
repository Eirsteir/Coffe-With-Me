
export function toggleLoading(component) {
    component.setState((prevState, props) => ({
        isLoading: !prevState.isLoading
    }));
};
