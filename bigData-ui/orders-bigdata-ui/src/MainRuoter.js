import { Routes,Route } from "react-router-dom";
import {Main} from "./Main"
import {Login} from "./Login"
import { Index } from "./home/Index"

function MainRuoter(){
    return (
        <>
            <Routes>
                <Route path="/home/*" element={<Main></Main>} >
                    <Route path="index" element={<Index />} />
                </Route>
                <Route path="/login" element={<Login></Login>} />
            </Routes>
        </>
    )
}

export default MainRuoter;