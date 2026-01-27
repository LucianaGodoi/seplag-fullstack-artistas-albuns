import { useState } from 'react';
import { authFacade } from '../../../app/facades/auth.facade';

export default function LoginPage() {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    async function handleLogin() {
        try {
            await authFacade.login(username, password);
            alert('Login realizado com sucesso!');
        } catch {
            alert('Erro ao logar');
        }
    }

    return (
        <div style={{ padding: 40 }}>
            <h2>Login</h2>

            <input
                placeholder="Usuário"
                value={username}
                onChange={e => setUsername(e.target.value)}
            />

            <br /><br />

            <input
                type="password"
                placeholder="Senha"
                value={password}
                onChange={e => setPassword(e.target.value)}
            />

            <br /><br />

            <button onClick={handleLogin}>
                Entrar
            </button>
        </div>
    );
}
