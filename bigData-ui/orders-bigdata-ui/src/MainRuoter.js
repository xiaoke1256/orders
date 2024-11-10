import { Routes,Route } from "react-router-dom";
import {Home} from "./Home"

function MainRuoter(){
    return (
        <>
            <Routes>
                <Route path="/home" element={<Home></Home>} />
            </Routes>
        </>
    )
}

export default MainRuoter;