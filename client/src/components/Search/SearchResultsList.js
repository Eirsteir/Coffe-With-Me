import React from "react";
import SearchResultsItem from "./SearchResultsItem";


const SearchResultsList = (props) => {
        
    const { userId, results } = props;
    
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

export default SearchResultsList;