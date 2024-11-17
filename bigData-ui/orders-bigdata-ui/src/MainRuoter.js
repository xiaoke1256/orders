import { Routes,Route } from "react-router-dom";
import {Home} from "./Home"
import {Login} from "./Login"

function MainRuoter(){
    return (
        <>
            <Routes>
                <Route path="/home" element={<Home></Home>} />
                <Route path="/login" element={<Login></Login>} />
            </Routes>
        </>
    )
}

export default MainRuoter;