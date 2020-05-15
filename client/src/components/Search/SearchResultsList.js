import React from "react";
import SearchResultsItem from "./SearchResultsItem";

const initialState = {
    results: []
};

class SearchResultsList
    extends React.Component {
    constructor(props) {
        super(props);
        this.state = initialState;
    }

    componentWillUpdate() {
        return (this.results = []);
    }
    
    render() {
        const { results } = this.props;

        return (
            <div style={{ marginTop: "1rem" }}>
                {results.map((user, i) => {
                    return (
                        <SearchResultsItem
                            key={i}
                            id={user.id}
                            username={user.username}
                            name={user.name}
                        />
                    );
                })}
            </div>
        );
    }
}

export default SearchResultsList
;