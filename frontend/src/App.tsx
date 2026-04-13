import {Route, Routes} from "react-router-dom";
import LoginPage from "./pages/LoginPage.tsx";
import AdminPage from "./pages/AdminPage.tsx";
import AccountantPage from "./pages/AccountantPage.tsx";
import EmployeePage from "./pages/EmployeePage.tsx";
import AuditorPage from "./pages/AuditorPage.tsx";

function App() {
    return(
        <Routes>
            <Route path={"/"} element={<LoginPage/>}/>
            <Route path={"/admin"} element={<AdminPage/>}/>
            <Route path={"/accountant"} element={<AccountantPage/>}/>
            <Route path={"/employee"} element={<EmployeePage/>}/>
            <Route path={"/auditor"} element={<AuditorPage/>}/>
        </Routes>
    )
}

export default App
