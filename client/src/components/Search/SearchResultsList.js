import React from "react";
import SearchResultsItem from "./SearchResultsItem";


class SearchResultsList
    extends React.Component {

    render() {
        const { userId, results } = this.props;
        
        return (
            <div style={{ marginTop: "1rem" }}>
                {results.map((user, i) => {

                    return (
                        <SearchResultsItem
                            key={i}
                            userId={userId}
                            user={user}
                        />
                    );
                })}
            </div>
        );
    }
}

export default SearchResultsList
;